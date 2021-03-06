package com.ticketswap.assessment.feature.search.domain.usecase

import com.ticketswap.assessment.feature.search.data.SpotifyRepository
import com.ticketswap.assessment.feature.search.domain.datamodel.ArtistDetailsDataModel
import com.ticketswap.assessment.feature.search.domain.datamodel.SpotifyDataModel
import com.ticketswap.assessment.feature.search.domain.failures.NullQueryFailure
import com.ticketswap.assessment.feature.search.domain.failures.SessionExpiredFailure
import com.ticketswap.authenticator.AuthGuard
import com.ticketswap.reactive.executor.PostExecutionThread
import com.ticketswap.reactive.executor.ThreadExecutor
import com.ticketswap.reactive.interactor.SingleUseCase
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class LoadArtistDetailsUseCase @Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val spotifyRepository: SpotifyRepository,
    private val authGuard: AuthGuard
) : SingleUseCase<ArtistDetailsDataModel, String>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseSingle(params: String?): Single<ArtistDetailsDataModel> {
        if (params == null) {
            return Single.error(NullQueryFailure())
        }
        if (!authGuard.userLoggedIn()) {
            return Single.error(SessionExpiredFailure())
        }

        return spotifyRepository.getArtistDetails(params, authGuard.getAuthToken())
    }
}
