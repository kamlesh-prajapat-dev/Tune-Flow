package org.example.project.domain.repository

import org.example.project.domain.model.Song
import org.example.project.utils.ApiResult

interface TrackRepository {
    suspend fun getTracks(): ApiResult<List<Song>>
}