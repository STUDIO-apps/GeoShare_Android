package uk.co.appsbystudio.geoshare.friends.profile.friends.pages

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import uk.co.appsbystudio.geoshare.R
import uk.co.appsbystudio.geoshare.friends.friendsadapter.FriendshipStatusAdapter
import java.util.ArrayList

class ProfileFriendsAllFragment : Fragment(), FriendshipStatusAdapter.Callback {

    private var auth: FirebaseAuth? = null
    private var databaseFriendsRef: DatabaseReference? = null
    private var databaseReference: DatabaseReference? = null

    private var friendAdapter: FriendshipStatusAdapter? = null

    var uid: String? = null

    private val friendId = ArrayList<String>()

    companion object {
        fun newInstance(uid: String?) = ProfileFriendsAllFragment().apply {
            arguments = Bundle().apply {
                putString("uid", uid)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_profile_friends_all, container, false)

        if (arguments != null) {
            uid = arguments!!.getString("uid")
        }

        auth = FirebaseAuth.getInstance()

        val database = FirebaseDatabase.getInstance()
        databaseReference = database.reference
        databaseFriendsRef = database.getReference("friends/$uid")

        val friendsAll: RecyclerView = view.findViewById(R.id.profile_friends_all_list)
        friendsAll.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        friendsAll.layoutManager = layoutManager

        getFriends()

        friendAdapter = FriendshipStatusAdapter(context, friendId, this)
        friendsAll.adapter = friendAdapter

        return view
    }

    private fun getFriends() {
        val friendsList = object : ChildEventListener {

            override fun onChildAdded(dataSnapshot: DataSnapshot, string: String?) {
                if (!friendId.contains(dataSnapshot.key) && dataSnapshot.key != auth?.currentUser?.uid) friendId.add(dataSnapshot.key!!)
                friendAdapter?.notifyDataSetChanged()
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, string: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                friendId.remove(dataSnapshot.key)
                friendAdapter?.notifyDataSetChanged()
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, string: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        databaseFriendsRef?.addChildEventListener(friendsList)
    }

    override fun onSendRequest(friendId: String?) {
        databaseReference?.child("pending")?.child(auth?.currentUser!!.uid)?.child(friendId!!)?.child("outgoing")?.setValue(true)
                ?.addOnFailureListener { success() }
                ?.addOnSuccessListener { success() }
        databaseReference?.child("pending")?.child(friendId!!)?.child(auth?.currentUser!!.uid)?.child("outgoing")?.setValue(false)
    }

    private fun success() {
        friendAdapter?.notifyDataSetChanged()
    }

}
