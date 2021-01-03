package com.morse.movie.base

import io.reactivex.Observable

interface MviViewModel < intents : MviIntent , state : MviState > {

    public fun processIntents (listOfIntents : Observable<intents>)

    public fun getStatus () : Observable<state>


}