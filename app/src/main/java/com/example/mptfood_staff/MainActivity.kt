package com.example.mptfood_staff

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mptfood_staff.databinding.ActivityMainBinding
import com.example.mptfood_staff.models.Order
import com.example.mptfood_staff.models.ProductInList
import com.example.mptfood_staff.models.OrderContent
import com.example.mptfood_staff.models.Product
import com.example.mptfood_staff.services.ServiceBuilder
import com.example.mptfood_staff.services.IRequest
import com.google.zxing.integration.android.IntentIntegrator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var listContent : ArrayList<ProductInList> = arrayListOf()
    lateinit var orderAdapter : OrderAdapter

    var resultLauncher = registerForActivityResult(QrCodeScannerContract()){
        if (!it.contents.isNullOrEmpty()){
            setOrderInfo(it.contents)
        }
    }

    private fun setOrderInfo(contents: String) {
        val serviceBuilder = ServiceBuilder.buildService(IRequest::class.java)
        val requestCall = serviceBuilder.getOrder(contents)
        requestCall.enqueue(object : Callback<Order>{
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.isSuccessful){
                    binding.orderNumber.text = "Заказ №${response.body()!!.id}"
                    binding.orderPrice.text = "Всего: ${response.body()!!.total}"
                    getOrderContent(response.body()!!.orderContents)
                    changeStatus(response.body()!!)
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun changeStatus(order: Order) {
        order.statusId = 5
        val serviceBuilder = ServiceBuilder.buildService(IRequest::class.java)
        val requestCall = serviceBuilder.changeStatus(order.id.toString(), order)
        requestCall.enqueue(object : Callback<Order>{
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.isSuccessful){
                    Toast.makeText(this@MainActivity, "Заказ №${order.id} завершен", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getOrderContent(orderContents: List<OrderContent>) {
        for (i in orderContents){
            val serviceBuilder = ServiceBuilder.buildService(IRequest::class.java)
            val requestCall = serviceBuilder.getProduct(i.productId.toString())
            requestCall.enqueue(object : Callback<Product>{
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    if (response.isSuccessful){
                        listContent.add(ProductInList(response.body()!!, i.quantity, i.orderId))
                        binding.listProducts.layoutManager = LinearLayoutManager(this@MainActivity)
                        binding.listProducts.setHasFixedSize(true)
                        binding.listProducts.adapter = orderAdapter
                    }
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        orderAdapter = OrderAdapter(this@MainActivity, listContent)
        supportActionBar?.title = "Получение заказа"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.qr_code_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.scan_qr -> resultLauncher.launch(IntentIntegrator(this))
        }

        return super.onOptionsItemSelected(item)
    }
}