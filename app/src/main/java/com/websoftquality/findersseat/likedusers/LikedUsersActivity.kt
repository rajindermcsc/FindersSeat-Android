package com.websoftquality.findersseat.likedusers

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.websoftquality.findersseat.R
import com.websoftquality.findersseat.configs.AppController
import com.websoftquality.findersseat.configs.SessionManager
import com.websoftquality.findersseat.datamodels.main.JsonResponse
import com.websoftquality.findersseat.interfaces.ApiService
import com.websoftquality.findersseat.interfaces.PaginationAdapterCallback
import com.websoftquality.findersseat.interfaces.ServiceListener
import com.websoftquality.findersseat.layoutmanager.OnItemSwiped
import com.websoftquality.findersseat.layoutmanager.SwipeableTouchHelperCallback
import com.websoftquality.findersseat.layoutmanager.touchelper.ItemTouchHelper
import com.websoftquality.findersseat.likedusers.model.LikedUserModel
import com.websoftquality.findersseat.utils.CommonMethods
import com.websoftquality.findersseat.utils.Enums.*
import com.websoftquality.findersseat.utils.PaginationScrollListener
import com.websoftquality.findersseat.utils.RequestCallback
import com.websoftquality.findersseat.views.customize.CustomDialog
import com.websoftquality.findersseat.views.main.IgniterPlusDialogActivity
import kotlinx.android.synthetic.main.activity_liked_users.*
import kotlinx.android.synthetic.main.header_layout.*
import javax.inject.Inject

class LikedUsersActivity : AppCompatActivity(), ServiceListener, PaginationAdapterCallback {
    private var likeUserCount: Int = 0

    private var itemPosition: Int = 0
    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var commonMethods: CommonMethods
    @Inject
    lateinit var customDialog: CustomDialog
    @Inject
    lateinit var sessionManager: SessionManager
    @Inject
    lateinit var gson: Gson

    lateinit var dialog: AlertDialog

    private lateinit var likedUserAdapter: LikedUserAdapter
    private lateinit var likedUserModel: LikedUserModel
    private var  subscriptionId : Int = 0
    private lateinit var gridLayoutManager: GridLayoutManager
    private var isLoading = false
    private var isLastPage = false
    private var totalPages = 0
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked_users)
        AppController.getAppComponent().inject(this)
        tv_header_title.text = resources.getString(R.string.see_who_likes_you)
        sv_root.visibility = View.GONE
        tv_empty_data.visibility = View.GONE

        initViews()
        getLikedProfiles()
    }

    /**
     * initViews
     */
    private fun initViews() {

        dialog = commonMethods.getAlertDialog(this)
        likedUserAdapter = LikedUserAdapter(this, this)
        gridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        rv_liked_users.itemAnimator = DefaultItemAnimator()

        val swipeableTouchHelperCallback = SwipeableTouchHelperCallback(object : OnItemSwiped {
            override fun onItemSwiped(adapterPosition: Int, viewHolder: RecyclerView.ViewHolder) {
                println("OnItemSwiped")


            }

            override fun onItemSwipedRight(position: Int) {


                itemPosition = position
                val userId = likedUserAdapter.getLikedUser().get(position).userId
                //val userId = likedUserModel.profilesLikedYou?.get(position)?.userId
                swipeProfile(userId, MATCH_LIKE, likedUserModel.usedSubscriptionId)
            }

            override fun onItemSwiping(direction: String?, position: Int) {
                println("Direction $direction")

            }

            override fun onItemSwipedDown() {
                println("onItemSwipedDown")
            }

            override fun onItemSwipedUp() {
                println("onItemSwipedUp")
            }

            override fun onItemSwipedLeft(position: Int) {

                itemPosition = position
                val userId = likedUserAdapter.getLikedUser().get(position).userId
                //val userId = likedUserModel.profilesLikedYou?.get(position)?.userId
                swipeProfile(userId, MATCH_NOPE, likedUserModel.usedSubscriptionId)
            }

        })

        val itemTouchHelper = ItemTouchHelper(swipeableTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rv_liked_users)

        rv_liked_users.layoutManager = gridLayoutManager
        rv_liked_users.adapter = likedUserAdapter

        tv_left_arrow.setOnClickListener {
            onBackPressed()
        }

        val resId = R.anim.layout_animation_bottom_up
        val animation = AnimationUtils.loadLayoutAnimation(this, resId)
        rv_liked_users.layoutAnimation = animation

        rv_liked_users.addOnScrollListener(object : PaginationScrollListener(gridLayoutManager) {
            override fun getTotalPageCount(): Int {
                return totalPages
            }

            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1
                getLikedProfiles()
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })

        btn_more_likes.setOnClickListener {
            val intent = Intent(this, IgniterPlusDialogActivity::class.java)
            intent.putExtra("startwith", "")
            intent.putExtra("type", "gold")
            startActivity(intent)
        }
    }


    /**
     * Call Api to get the Response
     */
    private fun getLikedProfiles() {
        if (commonMethods.isOnline(this)) {
            if (currentPage == 1) {
                commonMethods.showProgressDialog(this, customDialog)
            }
            apiService.likedProfiles(currentPage.toString(), sessionManager.token).enqueue(RequestCallback(REQ_LIKED_USER, this))
        } else {
            commonMethods.showMessage(this, dialog, resources.getString(R.string.network_failure))
        }
    }


    override fun onResume() {
        super.onResume()

        if (sessionManager.reloadLikedPage ) {
            sessionManager.reloadLikedPage = false
            likedUserAdapter.removeAll()
            currentPage = 1
            isLastPage = false
            sessionManager.refreshChatFragment = true
            getLikedProfiles()

        }
    }


    /**
     * handle the Success Form the api
     */


    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {
        commonMethods.hideProgressDialog()
        if (jsonResp!!.isOnline) {

            when (jsonResp.requestCode) {
                REQ_SWIPE_USER -> {
                    if (jsonResp.isSuccess) {
                        onSuccessSwipedUser(jsonResp)
                    } else {
                        commonMethods.showMessage(this, dialog, jsonResp.statusMsg)
                    }
                }
                REQ_LIKED_USER -> {
                    if (jsonResp.isSuccess) {
                        if (currentPage == 1) {
                            onSuccessGetLikedUsersList(jsonResp)
                        } else {
                            onLoadMoreLikedUsers(jsonResp)
                        }
                    } else if (currentPage == 1 && !TextUtils.isEmpty(jsonResp.statusMsg)) {
                        sv_root.visibility = View.GONE
                        tv_empty_data.visibility = View.VISIBLE
                    } else if (!TextUtils.isEmpty(jsonResp.statusMsg)) {
                        commonMethods.showMessage(this, dialog, jsonResp.statusMsg)
                    }
                }
            }


        } else {
            commonMethods.showMessage(this, dialog, resources.getString(R.string.network_failure))
        }
    }

    private fun onSuccessSwipedUser(jsonResp: JsonResponse) {

        likedUserAdapter.getLikedUser().removeAt(itemPosition)
        likedUserAdapter.notifyDataSetChanged()
        sessionManager.refreshChatFragment = true
        sessionManager.settingUpdate = true


        likeUserCount = likeUserCount-1

        if (likedUserModel.profilesLikedYou!!.size == 1) {
            tv_total_likes_count.text = resources.getString(R.string.users_like,likeUserCount.toString() )
        } else {
            tv_total_likes_count.text = resources.getString(R.string.users_likes, likeUserCount.toString())
        }

    }

    /**
     * Get Liked User lists
     */
    private fun onSuccessGetLikedUsersList(jsonResp: JsonResponse) {
        commonMethods.hideProgressDialog()
        likedUserModel = gson.fromJson(jsonResp.strResponse, LikedUserModel::class.java)
        subscriptionId = likedUserModel.usedSubscriptionId

        likedUserAdapter.setSubscriptionId(subscriptionId)

        if (likedUserModel.profilesLikedYou!!.size > 0) {
            sv_root.visibility = View.VISIBLE
            tv_empty_data.visibility = View.GONE

            likeUserCount = likedUserModel.totalLikes

            if (likedUserModel.profilesLikedYou!!.size == 1) {
                tv_total_likes_count.text = resources.getString(R.string.users_like, likeUserCount.toString())
            } else {
                tv_total_likes_count.text = resources.getString(R.string.users_likes, likeUserCount.toString())
            }
            totalPages = likedUserModel.totalPages
            likedUserModel.profilesLikedYou?.let { likedUserAdapter.addAll(it) }
            likedUserAdapter.notifyDataSetChanged()
            if (currentPage <= totalPages && totalPages > 1)
                likedUserAdapter.addLoadingFooter()
            else
                isLastPage = true
        } else {
            sv_root.visibility = View.GONE
            tv_empty_data.visibility = View.VISIBLE
        }
    }


    /**
     *  Load More Liked User List
     */
    private fun onLoadMoreLikedUsers(jsonResp: JsonResponse) {
        likedUserModel = gson.fromJson(jsonResp.strResponse, LikedUserModel::class.java)
        totalPages = likedUserModel.totalPages
        likedUserAdapter.removeLoadingFooter()
        isLoading = false

        likedUserModel.profilesLikedYou?.let { likedUserAdapter.addAll(it) }
        likedUserAdapter.notifyDataSetChanged()
        if (currentPage != totalPages)
            likedUserAdapter.addLoadingFooter()
        else
            isLastPage = true
    }


    /**
     * Handle When Failure
     */
    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        commonMethods.hideProgressDialog()
        commonMethods.showMessage(this, dialog, jsonResp!!.statusMsg)
    }

    /**
     * Call swipe profile API
     */
    private fun swipeProfile(userId: Int?, matchType: String, subscriptionId: Int) {

        if (commonMethods.isOnline(this)) {

            commonMethods.showProgressDialog(this, customDialog)
            if (userId != 0)
                apiService.swipeProfile(sessionManager.token, userId, matchType, subscriptionId).enqueue(RequestCallback(REQ_SWIPE_USER, this))
        } else {
            commonMethods.showMessage(this, dialog, resources.getString(R.string.network_failure))
        }


    }


    /**
     * OnReload the Page When Error Throws
     */
    override fun retryPageLoad() {
        getLikedProfiles()
    }
}