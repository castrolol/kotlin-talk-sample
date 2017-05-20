package br.com.devmt.gettingstartedkotlin

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import br.com.devmt.gettingstartedkotlin.models.User
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import org.jetbrains.anko.alert
import org.jetbrains.anko.find
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.onClick

class MainActivity : AppCompatActivity() {

    val gson by lazy { Gson() }

    val avatarImage by lazy { find<ImageView>(R.id.iv_avatar) }
    val usernameText by lazy { find<TextView>(R.id.tv_usename) }
    val nameText by lazy { find<TextView>(R.id.tv_name) }
    val companyText by lazy { find<TextView>(R.id.tv_empresa) }
    val localText by lazy { find<TextView>(R.id.tv_local) }
    val repoNumberText by lazy { find<TextView>(R.id.tv_repositories) }
    val followersText by lazy { find<TextView>(R.id.tv_followers) }
    val followingText by lazy { find<TextView>(R.id.tv_following) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        find<ImageButton>(R.id.bt_busca).onClick {
            mostrarDialogoBusca()
        }


    }

    fun mostrarDialogoBusca() {

        alert {
            //init view
            val view = layoutInflater.inflate(R.layout.dialog_serusername, null)
            view.find<ImageButton>(R.id.bt_close).onClick {
                dismiss()
            }

            //setup view

            val usernameText = view.find<TextView>(R.id.tv_usename)

            //setup dialog
            customView(view)
            positiveButton("Buscar") {
                buscarUsername(usernameText.text.toString())
            }

            show()

        }

    }

    fun buscarUsername(username: String) {

        indeterminateProgressDialog("Buscando") {
            show()

            Fuel.get("https://api.github.com/users/$username").responseString { request, response, result ->

                result.fold({ d ->
                    val user = gson.fromJson(d, User::class.java)

                    mostrarUsuario(user)

                }, { err ->

                    alert("Erro ao buscar usuario $username").show()

                })

                hide()
            }

        }


    }

    fun mostrarUsuario(user: User?) {

        if (user != null) {
            usernameText.text = user.login
            nameText.text = user.name
            companyText.text = user.company
            localText.text = user.location
            repoNumberText.text = user.public_repos.toString()
            followersText.text = user.followers.toString()
            followingText.text = user.following.toString()

            baixarAvatar(user.avatar_url)

        }
    }

    fun baixarAvatar(avatar_url: String) {

        Fuel.get(avatar_url).response { request, response, result ->

            result.fold({ bytes ->

                val image = BitmapDrawable(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
                avatarImage.setImageDrawable(  image )

            }, { err ->
                avatarImage.setImageResource( R.drawable.dev )


            })

        }

    }


}
