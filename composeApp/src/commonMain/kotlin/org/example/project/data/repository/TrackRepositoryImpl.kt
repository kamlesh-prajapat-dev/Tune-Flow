package org.example.project.data.repository

import kotlinx.datetime.LocalTime
import org.example.project.data.remote.api.JamendoApi
import org.example.project.domain.model.Song
import org.example.project.domain.repository.TrackRepository
import org.example.project.utils.ApiResult

class TrackRepositoryImpl : TrackRepository {
    override suspend fun getTracks(): ApiResult<List<Song>> {
        return when (val result = JamendoApi.getTracks()) {
            is ApiResult.Success -> {
                val tracks = result.data.results.map {
                    Song(
                        title = it.title,
                        artist = it.artistName,
                        duration = LocalTime.fromSecondOfDay(it.duration),
                        albumImage = it.albumImage,
                        audio = it.audio,
                        image = it.image,
                    )
                }
                ApiResult.Success(tracks)
            }
            is ApiResult.Error -> if (result.code != null) result else ApiResult.Error( "Network Error: A network error occurred. \n Please check your connection and try again.")

            else -> ApiResult.Error("Unknown error")
        }
    }
}