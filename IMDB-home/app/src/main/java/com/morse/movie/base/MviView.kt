package com.morse.movie.base
import io.reactivex.Observable


interface MviView < intents : MviIntent , outputs : MviState > {

    public fun render ( state : outputs )

    public fun collectOurIntents () : Observable<intents>
}