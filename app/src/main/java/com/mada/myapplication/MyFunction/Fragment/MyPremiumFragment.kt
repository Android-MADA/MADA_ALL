package com.mada.myapplication.MyFunction.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController/*
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams*/
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList
import com.mada.myapplication.CalenderFuntion.Model.subscribe
import com.mada.myapplication.CalenderFuntion.api.RetrofitServiceCalendar
import com.mada.myapplication.HomeFunction.api.RetrofitInstance
import com.mada.myapplication.MainActivity
import com.mada.myapplication.MyFunction.RetrofitServiceMy
import com.mada.myapplication.R
import com.mada.myapplication.StartFunction.Splash2Activity
import com.mada.myapplication.databinding.MyPremiumBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class MyPremiumFragment : Fragment() {

    private lateinit var binding: MyPremiumBinding
    lateinit var navController: NavController
    private val api = RetrofitInstance.getInstance().create(RetrofitServiceMy::class.java)
    private val token = Splash2Activity.prefs.getString("token", "")
    /*private lateinit var billingClient : BillingClient
    lateinit var billingFlowParams : BillingFlowParams*/

    /*private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    lifecycleScope.launch {
                        handlePurchase(purchase)
                    }
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
                Log.d("안녕하세요","상품 주문 취소")
            } else {
                // Handle any other error codes.
                Log.d("안녕하세요","구매요청 실패")
            }
        }

    suspend fun handlePurchase(purchase: Purchase) {

        if (purchase.purchaseState === Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                val ackPurchaseResult = withContext(Dispatchers.IO) {
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams.build(), acknowledgePurchaseResponseListener)
                }
            }
        }
    }
    private val acknowledgePurchaseResponseListener = AcknowledgePurchaseResponseListener { billingResult ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            // success
            binding.myPremiumBtn.text = "구독 중"
            val mainActivity = requireActivity() as MainActivity
            mainActivity.setPremium()
            val retrofit = Retrofit.Builder().baseUrl("http://www.madaumc.store/")
                .addConverterFactory(GsonConverterFactory.create()).build()
            val service = retrofit.create(RetrofitServiceCalendar::class.java)
            val call = service.patchSubscribe(token, subscribe(true))
            Log.d("viewmodelcheck", "?")
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d("viewmodelcheck", response.toString())
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("viewmodelcheck", t.toString())
                }
            })
        } else {
            // fail
        }
    }*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyPremiumBinding.inflate(inflater, container, false)

        /*billingClient = BillingClient.newBuilder(requireContext())
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {

                    val queryProductDetailsParams =
                        QueryProductDetailsParams.newBuilder()
                            .setProductList(
                                ImmutableList.of(
                                    QueryProductDetailsParams.Product.newBuilder()
                                        .setProductId("premium_1200")
                                        .setProductType(BillingClient.ProductType.SUBS)
                                        .build()))
                            .build()
                    billingClient.queryProductDetailsAsync(queryProductDetailsParams) {
                            billingResult,
                            productDetailsList ->
                        val productDetailsParamsList = productDetailsList.map { productDetails ->
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .setOfferToken(productDetails.subscriptionOfferDetails?.get(0)?.offerToken ?: "")
                                .build()
                        }
                        billingFlowParams = BillingFlowParams.newBuilder()
                            .setProductDetailsParamsList(productDetailsParamsList)
                            .build()
                    }
                    lifecycleScope.launch {
                        queryPurchaseAsync()
                    }

                } else {
                    Toast.makeText(requireContext(), "구글 플레이 연결 실패", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Toast.makeText(requireContext(), "구글 플레이 연결 실패", Toast.LENGTH_SHORT).show()
            }
        })*/


        return binding.root
    }
    suspend fun queryPurchaseAsync() {

        /*val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        // uses queryPurchasesAsync Kotlin extension function
        billingClient.queryPurchasesAsync(params) { purchasesResult, purchasesList ->
            if (purchasesResult.responseCode == BillingClient.BillingResponseCode.OK) {
                // Process the list of purchase
                if(purchasesList.isEmpty())
                    binding.myPremiumBtn.text = "결제하기"
                for (purchase in purchasesList) {
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        // The user has an active subscription
                        // Your code to handle the active subscription
                        val currentDate = Date()

                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = purchase.purchaseTime
                        calendar.add(Calendar.MONTH, 1) // 한 달 추가

                        val nextPaymentDate = calendar.time

                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        val formattedNextPaymentDate = sdf.format(nextPaymentDate)
                        binding.myPremiumBtn.text = "구독 중 ($formattedNextPaymentDate 까지)"
                    } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                        // The user has a pending subscription
                        // Handle accordingly, e.g., show a message to the user
                        binding.myPremiumBtn.text = "구독 보류 중"
                    }
                }
            } else {
                // Handle the billing result error
                val responseCode = purchasesResult.responseCode
                val debugMessage = purchasesResult.debugMessage
                Toast.makeText(requireContext(), "결제 이력을 불러오는 중에 오류 발생", Toast.LENGTH_SHORT).show()
            }
        }*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = binding.navHostFragmentContainer.findNavController()

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_myPremiumFragment_to_fragMy)
        }

        binding.myPremiumBtn.setOnClickListener {
            /*val billingResult = billingClient.launchBillingFlow(requireActivity(), billingFlowParams)*/
        }
    }



}


