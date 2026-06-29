package com.route.data.di.cart

import com.route.data.dataSource.api.cart.CartService
import com.route.data.dataSource.cart.CartRemoteDataSourceImpl
import com.route.data.repository.cart.CartRepositoryImpl
import com.route.domain.repository.CartRemoteDataSource
import com.route.domain.repository.CartRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CartModule {
    @Provides
    @Singleton
    fun provideCartService(retrofit: Retrofit): CartService {
        return retrofit.create(CartService::class.java)
    }

    @Provides
    @Singleton
    fun provideCartRemoteDataSource(service: CartService): CartRemoteDataSource {
        return CartRemoteDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideCartRepository(remoteDataSource: CartRemoteDataSource): CartRepository {
        return CartRepositoryImpl(remoteDataSource)
    }
}