package com.route.e_commercec43gsunwed.screens.home.composable.categories

import com.route.domain.model.Result
import com.route.domain.model.categories.CategoryItem
import com.route.domain.model.categories.SubCategoryItem
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface CategoriesContract {
    interface ViewModel {
        val states: StateFlow<States>
        val events: SharedFlow<Events>
        fun handleActions(action: Action)
    }

    sealed interface Action {
        data object Idle : Action
        data object GetCategories : Action
        data class SelectCategory(val category: CategoryItem?) : Action
        data class SelectSubCategory(val subCategory: SubCategoryItem?) : Action
        data object ClickedOnCart : Action
        data object ClickedOnSearch : Action
    }

    data class States(
        val categoriesList: Result<List<CategoryItem>>? = null,
        val subCategoriesList: Result<List<SubCategoryItem>>? = null,
        val cartItemsCount: Int = 0
    )

    sealed interface Events {
        data object Idle : Events
        data object NavigateToCart : Events
        data object NavigateToSearch : Events
        data class NavigateToProducts(val subCategoryId: String?) : Events
    }


}