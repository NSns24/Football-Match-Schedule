package com.ns.footballmatchschedule.util

import android.support.test.espresso.IdlingResource

object MainIdlingResource : IdlingResource {

    private var callback: IdlingResource.ResourceCallback? = null
    private var counter: Int = 0

    override fun getName(): String {
        return "${MainIdlingResource::class.java.simpleName}"
    }

    override fun isIdleNow(): Boolean {
        return counter == 0
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }

    fun increment() {
        counter = 1
    }

    fun decrement() {
        counter = 0

        if(isIdleNow) {
            backgroundWorkDone()
        }
    }

    private fun backgroundWorkDone() {
        callback?.onTransitionToIdle()
    }

}