# simple-chat
Program percakapan sederhana sebagai tugas Mata Kuliah **Keamanan Informasi dan Jaringan** (KIJ) 2014/2015.

## Penjelasan program
Simple-chat menerapkan _socket programming_. Ada dua komponen utama yaitu _server_ dan _client_. _Server_ sendiri ditulis dengan bahasa C dan _client_ dengan bahasa Java. Simple-chat memiliki prosedur penggunaan sebagai berikut:
* Pertama, client akan memasukkan _username_ terlebih dahulu lalu melakukan _connect_ ke _server_,
* Setelah terhubung ke _server_, _server_ akan menampilkan siapa saja pengguna lain yang sedang _online_,
* _Client_ memilih salah satu pengguna dari daftar tersebut untuk mengirimkan pesan (_private message_),
* _Server_ menerima dan mengirimkan pesan ke pengguna lain yang dituju, dan
* Pesan akan di tampilkan.

kelengkapan dokumentasi penjelasan program di dalam [screenhots](https://github.com/meisyal/simple-chat/tree/master/Screenshots).

# Fitur
Adapun fitur yang diakomodasi aplikasi percakapan sederhana ini, antara lain:
* _Private message_
* Daftar _online user_
* Keamanan (menggunakan RC4 dan RSA _key-exchange_)
* Integritas data (menggunakan MD5)

## Hal yang harus dikerjakan
Daftar hal yang harus dikerjakan saat ini, antara lain:
- [x] Pendefinisian [protokol](https://github.com/meisyal/simple-chat/blob/master/Client/Protokol.txt)
- [x] Pembuatan chat-server
- [x] Pembuatan chat-client (GUI)
- [x] Uji coba
- [ ] Dokumentasi dan diskusi

## Kontributor
Adapun kontributor yang ikut berkontribusi dalam pengerjaan program ini, antara lain:
* [Dimas Riska Hadi](https://github.com/dimasdevo)
* [Andrias Meisyal Y](https://github.com/meisyal)
* [Wimbra Agra Wicesa](https://github.com/wimbaagra)
* [Biandina Meidyani](https://github.com/biandina)
