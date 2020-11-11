package com.justice.noteapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.*
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.justice.noteapp.MainFragment.Companion.COLLECTION_HIGH
import com.justice.noteapp.MainFragment.Companion.COLLECTION_LOW
import com.justice.noteapp.MainFragment.Companion.COLLECTION_NOTES
import com.justice.noteapp.MainFragment.Companion.TAG
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var mainActivity: MainActivity
    private var viewsReady: Boolean? = null

    companion object {
        const val TAG = "MainFragment"
        const val COLLECTION_HIGH = "high"
        const val COLLECTION_MEDIUM = "medium"
        const val COLLECTION_LOW = "low"
        const val COLLECTION_NOTES = "notes"
        const val RC_SIGN_IN = 3

        @JvmField
        val fragments: MutableList<Fragment> = ArrayList()

        @JvmField
        val fragmentsName: MutableList<String> = ArrayList()
    }

    private lateinit var navController: NavController

    //    private var toolbar: Toolbar? = null
//    private var tabLayout: TabLayout? = null
//    private var viewPager: ViewPager? = null
    private val mediumFragment: Fragment = MediumFragment()
    private val highFragment: Fragment = HighFragment()
    private val lowFragment: Fragment = LowFragment()
    private var position = 0
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        viewsReady = true
//        mainActivity = activity as MainActivity
//        navController = findNavController()
//        setUpToolBar()
//        checkIfWeAreLoginIn()
//        setUpViewPager()
//        setOnClickListeners()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewsReady = true
        mainActivity = activity as MainActivity
        navController = findNavController()
        setUpToolBar()
        checkIfWeAreLoginIn()
        setUpViewPager()
        setOnClickListeners()
    }

    private fun setUpToolBar() {
        mainActivity.setSupportActionBar(toolBar)
        setHasOptionsMenu(true)
    }

    private fun checkIfWeAreLoginIn() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            //we are not logged in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    GoogleBuilder().build(),
                                    EmailBuilder().build(),
                                    PhoneBuilder().build(),
                                    AnonymousBuilder().build()))
                            .setTosAndPrivacyPolicyUrls("https://www.linkedin.com/in/JusticeEli",
                                    "https://github.com/JusticeEli/").build(),
                    RC_SIGN_IN)
        } else {

            try {
                setBadgeUpdateListener()
            } catch (e: Exception) {
                Log.e(TAG, "checkIfWeAreLoginIn: ", e)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            // Successfully signed in
            if (resultCode == Activity.RESULT_OK) {

                recreateFragment()
                Log.d(TAG, "onActivityResult: sign in success")
                return
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showToast("sign in cancelled")
                }
                if (response!!.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                    showToast("no internet connection")
                }
                showToast("unknown error")
                Log.e(TAG, "Sign-in error: ", response.error)
                Log.d(TAG, "onActivityResult: finish of onActivityResult")
                goBack();
            }
        }
    }

    private fun recreateFragment() {

        findNavController().navigate(R.id.mainFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.mainFragment, true).build())


    }


    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun setBadgeUpdateListener() {

        firebaseFirestore.collection(COLLECTION_NOTES).document(FirebaseAuth.getInstance().uid!!).collection(COLLECTION_MEDIUM).addSnapshotListener(EventListener { queryDocumentSnapshots, e ->
            if (e != null) {
                Log.e(TAG, "onEvent: Error" + e.message)
                Toast.makeText(context, "Error" + e.message, Toast.LENGTH_LONG).show()
                return@EventListener
            }
            Log.d(TAG, "onSuccess: loaded medium")
            Log.d(TAG, "setBadgeUpdateListener: notifying data set of the view pager adapter")

            if (viewsReady!!) {
                viewPager.adapter!!.notifyDataSetChanged()
                tabLayout?.getTabAt(0)!!.orCreateBadge.number = queryDocumentSnapshots!!.size()

            }


        })

        firebaseFirestore.collection(COLLECTION_NOTES).document(FirebaseAuth.getInstance().uid!!).collection(COLLECTION_HIGH).addSnapshotListener(EventListener { queryDocumentSnapshots, e ->
            if (e != null) {
                Log.e(TAG, "onEvent: Error" + e.message)
                Toast.makeText(context, "Error" + e.message, Toast.LENGTH_LONG).show()
                return@EventListener
            }
            Log.d(TAG, "onSuccess: loaded high")
            if (viewsReady!!) {
                tabLayout?.getTabAt(1)!!.orCreateBadge.number = queryDocumentSnapshots!!.size()

            }

        })
        firebaseFirestore.collection(COLLECTION_NOTES).document(FirebaseAuth.getInstance().uid!!).collection(COLLECTION_LOW).addSnapshotListener(EventListener { queryDocumentSnapshots, e ->
            if (e != null) {
                Log.e(TAG, "onEvent: Error" + e.message)
                Toast.makeText(context, "Error" + e.message, Toast.LENGTH_LONG).show()
                return@EventListener
            }
            Log.d(TAG, "onSuccess: loaded low")
            if (viewsReady!!) {
                tabLayout?.getTabAt(2)!!.orCreateBadge.number = queryDocumentSnapshots!!.size()

            }

        })


    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: called")
        //       setBadgeUpdateListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: called")
        viewsReady = false

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: called")
//        addSnapshotListener1.remove()
//        addSnapshotListener2.remove()
//        addSnapshotListener3.remove()
    }

    private fun setOnClickListeners() {
        viewPager!!.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                this@MainFragment.position = position

                Log.d(TAG, "onPageSelected: page: " + position + " selected")
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun setUpViewPager() {
        //incase oncreate has been called more than one time resulting to many tabs
        fragments.clear()
        fragmentsName.clear()
        fragments.add(mediumFragment)
        fragments.add(highFragment)
        fragments.add(lowFragment)
        fragmentsName.add("medium")
        fragmentsName.add("high")
        fragmentsName.add("low")
        val adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, 0)
        viewPager!!.adapter = adapter
        tabLayout!!.setupWithViewPager(viewPager)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)


        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected: invoked")
        when (item.itemId) {
            R.id.addMenu -> {

                Log.d(TAG, "onOptionsItemSelected: going to add note fragment")
                val action = MainFragmentDirections.actionMainFragmentToAddNoteFragment(null)
                navController.navigate(action)
            }
            R.id.deleteMenu -> deleteAllNotes()
           R.id.logoutMenu -> {
               viewPager.adapter?.notifyDataSetChanged()
               ( fragments.get(0) as MediumFragment).noteAdapter.notifyDataSetChanged()
           }
//            R.id.logoutMenu -> logout()
        }
        return super.onOptionsItemSelected(item)

    }

    private fun logout() {
        Log.d(TAG, "logout: clicked")
        AuthUI.getInstance()
                .signOut(requireContext())
                .addOnCompleteListener { // user is now signed out
                    Log.d(TAG, "onComplete: finish of logout")
                    mainActivity.onBackPressed()
                }
    }

    private fun goBack() {
        Log.d(TAG, "goBack: going back")
        mainActivity.onBackPressed()


//        activity?.supportFragmentManager?.popBackStackImmediate()
    }

    private fun deleteAllNotes() {
        Log.d(TAG, "deleteAllNotes: deletting all notes")
        when (position) {
            0 -> deleteMediumNotes()
            1 -> deleteHighNotes()
            2 -> deleteLowNotes()

        }
    }

    private fun deleteMediumNotes() {
        firebaseFirestore.collection(COLLECTION_NOTES).document(FirebaseAuth.getInstance().uid!!).collection(COLLECTION_MEDIUM).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (documentSnapshot in task.result!!) {
                    documentSnapshot.reference.delete()
                }
            } else {
                Toast.makeText(context, "Error: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteLowNotes() {
        firebaseFirestore.collection(COLLECTION_NOTES).document(FirebaseAuth.getInstance().uid!!).collection(COLLECTION_LOW).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (documentSnapshot in task.result!!) {
                    documentSnapshot.reference.delete()
                }
            } else {
                Toast.makeText(context, "Error: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteHighNotes() {
        firebaseFirestore.collection(COLLECTION_NOTES).document(FirebaseAuth.getInstance().uid!!).collection(COLLECTION_HIGH).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (documentSnapshot in task.result!!) {
                    documentSnapshot.reference.delete()
                }
            } else {
                Toast.makeText(context, "Error: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}


