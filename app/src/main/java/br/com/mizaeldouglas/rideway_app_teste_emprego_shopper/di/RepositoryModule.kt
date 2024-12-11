package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.di

import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.api.IApiService
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.repository.IRideRepository
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.data.repository.RideRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRideRepository(apiService: IApiService): IRideRepository {
        return RideRepositoryImpl(apiService)
    }
}