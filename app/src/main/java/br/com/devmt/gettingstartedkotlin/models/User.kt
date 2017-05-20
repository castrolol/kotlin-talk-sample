package br.com.devmt.gettingstartedkotlin.models

/**
 * Created by castrolol on 20/05/17.
 */


data class User(

        var avatar_url: String,
        var login: String,
        var name: String,
        var company: String,
        var location: String,
        var public_repos: Int,
        var followers: Int,
        var following: Int
)