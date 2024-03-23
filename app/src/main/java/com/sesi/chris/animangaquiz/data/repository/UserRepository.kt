package com.sesi.chris.animangaquiz.data.repository

import com.sesi.chris.animangaquiz.data.datasource.UserDataSource
import com.sesi.chris.animangaquiz.data.dto.UpdateGemsDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Error
import javax.inject.Inject

class UserRepository @Inject constructor(private val service: UserDataSource) {
    fun updateGem(
        gemsInfo: UpdateGemsDto,
        action: UserAction
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            service.updateGems(gemsInfo) { error, isUpdated ->
                if (error != null) {
                    action.showError(error)
                    return@updateGems
                }
                if (isUpdated) {
                    action.isUpdated(true)
                }
            }
        }
    }
}

interface UserAction {
    fun showError(error: String)
    fun isUpdated(isUpdated: Boolean)
}