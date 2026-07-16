package com.route.data.di.wishlist

import com.route.data.dataSource.api.wishlist.WishlistService
import com.route.data.dataSource.wishlist.WishlistRemoteDataSourceImpl
import com.route.data.repository.wishlist.WishlistRepositoryImpl
import com.route.domain.repository.WishlistRemoteDataSource
import com.route.domain.repository.WishlistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WishlistModule {

    @Provides
    @Singleton
    fun provideWishlistService(retrofit: Retrofit): WishlistService =
        retrofit.create(WishlistService::class.java)

    @Provides
    @Singleton
    fun provideWishlistRemoteDataSource(
        service: WishlistService
    ): WishlistRemoteDataSource = WishlistRemoteDataSourceImpl(service)

    @Provides
    @Singleton
    fun provideWishlistRepository(
        remoteDataSource: WishlistRemoteDataSource
    ): WishlistRepository = WishlistRepositoryImpl(remoteDataSource)
}