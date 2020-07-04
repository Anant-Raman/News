package com.globallogic.sampleapp.framework.network

interface IViewApiListener {

    fun notifyViewOnSuccess(`object`: Any?, type: Int)

    fun notifyViewOnFailure(`object`: Any?, type: Int)

}