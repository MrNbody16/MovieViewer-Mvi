package com.mr_nbody16.helperClasses

import androidx.navigation.NavOptions
import com.mr_nbody16_movieViwer_mvi.R

object NavOpt {
    fun build() :NavOptions {
        return NavOptions.Builder().setEnterAnim(R.anim.enter_from_left).setExitAnim(R.anim.exit_to_right)
            .setPopEnterAnim(R.anim.enter_from_right).setPopExitAnim(R.anim.exit_to_left)
            .build()
    }
}