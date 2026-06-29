package com.route.e_commercec43gsunwed.screens.home.composable.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.route.domain.cart.CartManager
import com.route.domain.usecases.category.GetCategoriesUseCase
import com.route.domain.usecases.category.GetSubCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    val getCategoriesUseCase: GetCategoriesUseCase,
    val getSubCategoriesUseCase: GetSubCategoriesUseCase,
    private val cartManager: CartManager
) : ViewModel(), CategoriesContract.ViewModel {
    private val _states =
        MutableStateFlow(CategoriesContract.States())
    override val states: StateFlow<CategoriesContract.States>
        get() = _states
    private val _events =
        MutableSharedFlow<CategoriesContract.Events>()
    override val events: SharedFlow<CategoriesContract.Events>
        get() = _events


    init {
        viewModelScope.launch {

            cartManager.count.collect {

                _states.value =
                    _states.value.copy(
                        cartItemsCount = it
                    )

            }

        }
    }

    override fun handleActions(action: CategoriesContract.Action) {
        viewModelScope.launch {
            when (action) {
                CategoriesContract.Action.ClickedOnCart -> {
                    _events.emit(CategoriesContract.Events.NavigateToCart)
                }

                CategoriesContract.Action.ClickedOnSearch -> {}
                CategoriesContract.Action.Idle -> {}
                is CategoriesContract.Action.SelectCategory -> {
                    action.category?.id?.let {
                        getSubCategories(it)
                    }
                }

                is CategoriesContract.Action.SelectSubCategory -> {
                    _events.emit(
                        CategoriesContract.Events.NavigateToProducts(
                            subCategoryId = action.subCategory?.id
                        )
                    )
                }

                CategoriesContract.Action.GetCategories -> getCategories()
            }

        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            getCategoriesUseCase.invoke().collect {
                _states.value = _states.value.copy(categoriesList = it)
            }
        }
    }

    private fun getSubCategories(categoryId: String) {
        viewModelScope.launch {
            getSubCategoriesUseCase.invoke(categoryId).collect {
                _states.value = _states.value.copy(subCategoriesList = it)
            }
        }
    }
}
