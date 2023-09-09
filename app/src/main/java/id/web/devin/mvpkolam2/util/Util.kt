package id.web.devin.mvpkolam2.util

import android.app.Activity
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import id.web.devin.mvpkolam2.R
import id.web.devin.mvpkolam2.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.NumberFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


fun ImageView.loadImage(url:String, progressBar: ProgressBar){
    Picasso.get()
        .load(url)
        .resize(1000,1300)
        .centerCrop()
        .error(R.drawable.baseline_error_24)
        .into(this, object :Callback{
            override fun onSuccess() {
                progressBar.visibility = View.GONE
            }
            override fun onError(e: Exception?) {
                progressBar.visibility = View.GONE
            }
        })
}

fun calculateTimeDifference(targetDateTime: LocalDateTime): Duration {
    val currentDateTime = LocalDateTime.now()
    return Duration.between(currentDateTime, targetDateTime)
}
fun SisaWaktu(tanggal:String):Long{
    val targetDateTimeString = add24HoursToDateTime(tanggal)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val targetDateTime = LocalDateTime.parse(targetDateTimeString, formatter)

    // Hitung sisa waktu
    val timeDifference = calculateTimeDifference(targetDateTime)

    // Output sisa waktu dalam format jam, menit, dan detik
    val hours = timeDifference.toHours()
    val minutes = (timeDifference.toMinutes() % 60).toInt()
    val seconds = (timeDifference.seconds % 60).toInt()
    var milidetik = hours * 60 * 60 * 1000 + minutes * 60 * 1000 + seconds * 1000

    return milidetik
}

fun add24HoursToDateTime(inputDateTime: String): String {
    // Tentukan format tanggal dan waktu yang sesuai
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    // Parse inputDateTime menjadi objek Date
    val date = inputFormat.parse(inputDateTime)

    // Tambahkan 24 jam ke objek Date
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.HOUR_OF_DAY, 24)

    // Format kembali objek Date ke string
    val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return outputFormat.format(calendar.time)
}

fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    format.maximumFractionDigits = 0
    format.currency = Currency.getInstance("IDR")
    val formattedAmount = format.format(amount)
    return formattedAmount.replace(Regex("\\.00$"), "")
}

fun calculateTotalYears(dateString: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateString, formatter)
    val currentDate = LocalDate.now()
    val period = Period.between(date, currentDate)
    return period.years
}

fun formatDate(inputDate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("id","ID"))

    val date = inputFormat.parse(inputDate)
    return outputFormat.format(date)
}

fun formatDate2(inputDate: String): String {
    val inputFormat = SimpleDateFormat("d MMMM yyyy", Locale("id","ID"))
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val date = inputFormat.parse(inputDate)
    return outputFormat.format(date)
}

fun formatDateTime(inputDate: String):String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

    val dateTime = LocalDateTime.parse(inputDate, inputFormatter)
    val formattedDateTime = dateTime.format(outputFormatter)

    return formattedDateTime
}

object EncryptionUtils{
    private const val secretKey = "mySecretKey"
    fun encrypt(text: String): String {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        val secretKeySpec = SecretKeySpec(generateKey(secretKey), "AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        val encryptedBytes = cipher.doFinal(text.toByteArray(StandardCharsets.UTF_8))
        return android.util.Base64.encodeToString(encryptedBytes, android.util.Base64.DEFAULT)
    }

    private fun generateKey(secretKey: String): ByteArray? {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(secretKey.toByteArray(StandardCharsets.UTF_8))
    }
}

object Global {
    const val SHARED_PREFERENCES = "LOKOWAI"
    const val SHARED_PREF_EMAIL = "EMAIL"
    const val SHARED_PREF_KEY_ROLE = "ROLE"
    fun getEmail(context: Context): String? {
        return context.getSharedPreferences(SHARED_PREFERENCES, Activity.MODE_PRIVATE)
            .getString(SHARED_PREF_EMAIL, null)
    }

    fun getRole(context: Context): String? {
        return context.getSharedPreferences(SHARED_PREFERENCES, Activity.MODE_PRIVATE)
            .getString(SHARED_PREF_KEY_ROLE, null)
    }
}

interface RajaOngkirService {
    @POST("cost")
    fun calculateShippingCosts(@Body request: ShippingCostRequest): Call<ShippingResponse>
}

interface UploadService {
    @Multipart
    @POST("uploadgambar.php")
    fun uploadImage(@Part image: MultipartBody.Part, @Part("folder") folder: RequestBody): Call<UploadResponse>
}

interface AuthPresenterListener {
    fun showError(message: String)
    fun  register()
}

interface KolamPresenterListener {
    fun showError(message: String)
    fun showKolam(kolam: List<Kolam>)
    fun success()
}

interface KolamDetailPresenterListener {
    fun showError(message: String)
    fun showKolam(kolam: List<Kolam>)
}

interface PelatihPresenterListener {
    fun showError(message: String)
    fun showPelatih(pelatih: List<Pelatih>)
    fun succes()
}

interface ProductPresenterListener {
    fun showError(message: String)
    fun showProduk(produk: List<Produk>)
    fun success()
}

interface ProfilePresenterListener {
    fun showError(message: String)
    fun showProfile(profile: List<Pengguna>)
    fun success()
}

interface TransaksiPresenterListener {
    fun showError(message: String)
    fun showTransaksi(transaksi: List<Transaction>)
    fun success()
}

interface CartPresenterListener {
    fun showError(message: String)
    fun showCart(cart: List<Cart>)
}

interface CartDetailPresenterListener {
    fun showError(message: String)
    fun showCart(cart: List<Cart>)
}

interface ShippingPresenterListener {
    fun showError(message: String)
    fun showShipping(shipping: ShippingResponse?)
}

interface UploadPresenterListener {
    fun uploadError(message: String)
    fun UploadSuccess(upload: UploadResponse)
}

interface ProvinsiPresenterListener {
    fun showError(message: String)
    fun showProvinsi(provinsi: List<Provinsi>)
}

