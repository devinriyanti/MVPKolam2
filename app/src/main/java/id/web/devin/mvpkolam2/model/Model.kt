package id.web.devin.mvpkolam2.model

import com.google.gson.annotations.SerializedName

data class Provinsi(
    val idprovinsi:Int,
    val nama:String,
    val kota:ArrayList<Kota>
)

data class Kota (
    val idkota:Int,
    @SerializedName("nama_kota")
    val nama: String,
    @SerializedName("type")
    val tipe:String,
    val kode_pos:String
)
data class Pelatih(
    val id:String?,
    val nama:String?,
    @SerializedName("tanggal_lahir")
    val tglLahir:String?,
    val kontak:String?,
    @SerializedName("mulai_karir")
    val tglKarir:String?,
    val deskripsi:String?,
    val jenis_kelamin: String?,
    @SerializedName("url_gambar")
    val gambarUrl: String?
)

data class Produk(
    @SerializedName("id")
    val idproduk:String?,
    val kolam:String,
    val nama:String?,
    val kota: String?,
    val deskripsi: String?,
    @SerializedName("kuantitas")
    var qty: Int?,
    val harga:Double?,
    val diskon:Double?,
    @SerializedName("url_gambar")
    val gambarUrl: String?,
    val berat:Int?,
    val status:String?
)

data class Kolam(
    val id: String?,
    val nama: String?,
    val alamat: String?,
    val deskripsi: String?,
    @SerializedName("url_gambar")
    val gambarUrl: String?,
    val is_maintenance: String?,
    val status:String?,
    val kota: String?,
    @SerializedName("url_lokasi")
    val lokasi:String?,
    @SerializedName("email_pengguna")
    val admin: String?,
    @SerializedName("product")
    val produk:ArrayList<Produk>,
    val pelatih:ArrayList<Pelatih>
)

data class Pengguna(
    val email: String?,
    val nama:String?,
    val alamat:String?,
    val telepon:String?,
    val jenis_kelamin:String?,
    val norekening: String?,
    val nama_rekening:String?,
    @SerializedName("tanggal_lahir")
    val tglLahir: String?,
    @SerializedName("password")
    val pwd:String?,
    val role:Role,
    @SerializedName("nama_kota")
    val kota: String,
    val idkota: String
)

data class Transaction(
    val id: String,
    val namaKolam:String,
    val email:String,
    val idkota:String,
    @SerializedName("status_transaksi")
    val status:StatusTransaksi,
    val produk: ArrayList<ProductTransaction>
)

data class ProductTransaction(
    val id: String,
    val idkolam: String,
    val tanggal: String,
    val total_harga: Double,
    val alamat_kirim: String,
    val status_transaksi: String,
    val urlBukti: String,
    @SerializedName("tanggal_pembayaran")
    val tanggalBayar:String,
    val no_resi:String,
    val idkota: Int,
    val email_pengguna: String,
    val namaKolam: String,
    @SerializedName("email")
    val emailAdmin: String,
    val idproduk: String,
    val nama: String,
    val qty: Int,
    val harga: Double,
    val diskon: Double,
    val berat: Int,
    @SerializedName("url_gambar")
    val gambar: String
)

data class Cart(
    @SerializedName("IdKolam")
    val id:String,
    val nama:String,
    val idkota:String,
    val produk: ArrayList<ProdukCart>
)

data class ProdukCart(
    val idkeranjangs: Int,
    val idkolam:String,
    val total_harga:Double,
    @SerializedName("email_pengguna")
    val email:String,
    val idproduk:String,
    val namaProduk:String,
    val harga:Double,
    val qty: Int,
    val diskon: Double,
    @SerializedName("url_gambar")
    val gambar:String,
    val berat:Int,
    val norekening:String,
    val nama_rekening: String?
)

data class UploadResponse(
    val result: String,
    val message:String
)

data class ShippingCostRequest(
    val origin: String,
    val destination: String,
    val weight: Int,
    val key: String,
    val courier:String
)

data class ShippingResponse(
    val rajaongkir: RajaongkirResponse
)

data class RajaongkirResponse(
    val query: Query,
    val status: Status,
    val origin_details: LocationDetails,
    val destination_details: LocationDetails,
    val results: List<ShippingService>
)

data class Query(
    val origin: String,
    val destination: String,
    val weight: Int,
    val key: String,
    val courier: String
)

data class Status(
    val code: Int,
    val description: String
)

data class LocationDetails(
    val city_id: String,
    val province_id: String,
    val province: String,
    val type: String,
    val city_name: String,
    val postal_code: String
)

data class ShippingService(
    val code: String,
    val name: String,
    val costs: List<ShippingCost>
)

data class ShippingCost(
    val service: String,
    val description: String,
    val cost: List<CostDetail>
)

data class CostDetail(
    val value: Int,
    val etd: String,
    val note: String
)

enum class Gender(val displayText:String) {
    Other("--Pilih Jenis Kelamin--"),
    Laki_Laki("Laki-Laki"),
    Perempuan("Perempuan")
}

enum class Role{
    Pengguna,
    Admin
}

enum class StatusTransaksi{
    Diproses,
    Dikirim,
    Diterima,
    Dibatalkan
}