package com.route.data.di.user

import com.route.data.dataSource.api.user.UserService
import com.route.data.model.user.UserRemoteDataSourceImpl
import com.route.data.repository.user.UserRepositoryImpl
import com.route.domain.repository.AuthRepository
import com.route.domain.repository.UserRemoteDataSource
import com.route.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(service: UserService): UserRemoteDataSource =
        UserRemoteDataSourceImpl(service)

    @Provides
    @Singleton
    fun provideUserRepository(
        remoteDataSource: UserRemoteDataSource,
        authRepository: AuthRepository
    ): UserRepository = UserRepositoryImpl(remoteDataSource, authRepository)
}