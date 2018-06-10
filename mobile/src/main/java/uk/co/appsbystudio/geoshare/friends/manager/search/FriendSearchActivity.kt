package uk.co.appsbystudio.geoshare.friends.manager.search

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_friend_search.*
import kotlinx.android.synthetic.main.activity_profile.*
import uk.co.appsbystudio.geoshare.R

class FriendSearchActivity : AppCompatActivity(), FriendSearchView, FriendSearchAdapter.Callback {

    private var presenter: FriendSearchPresenter? = null

    private var searchAdapter: FriendSearchAdapter? = null

    private val userMap = LinkedHashMap<String, String>()

    private var oldest: String = ""
    private var current: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_search)

        presenter = FriendSearchPresenterImpl(this, FriendSearchInteractorImpl())
        searchAdapter = FriendSearchAdapter(this, userMap, this)

        image_back_button_profile?.setOnClickListener { finish() }

        recycler_results_search.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FriendSearchActivity)
            adapter = searchAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (!recyclerView.canScrollVertically(1) && current != "") {
                        presenter?.scroll(oldest, current)
                    }
                }
            })
        }

        search_view_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                presenter?.search(s)
                current = s
                return false
            }
        })
    }

    override fun onSendRequest(friendId: String) {
        presenter?.request(friendId)
    }

    override fun add(uid: String, name: String) {
        if (!userMap.contains(uid)) {
            userMap[uid] = name
            searchAdapter?.notifyDataSetChanged()
            oldest = name
        }
    }

    override fun clear() {
        searchAdapter?.notifyItemRangeRemoved(0, userMap.size)
        userMap.clear()
    }

    override fun updateRecycler() {
        searchAdapter?.notifyDataSetChanged()
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}