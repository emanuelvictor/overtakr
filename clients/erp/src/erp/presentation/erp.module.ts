import {CUSTOM_ELEMENTS_SCHEMA, NgModule, provideZoneChangeDetection} from '@angular/core';

import {CommonModule, registerLocaleData} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {AppRootComponent} from './app-root.component';
import {AuthenticatedViewComponent} from './authenticated/authenticated-view.component';
import {ErpRoutingModule} from './erp.routes';
import {HTTP_INTERCEPTORS, HttpClientJsonpModule, HttpClientModule} from '@angular/common/http';
import {Interceptor} from '../../common/application/interceptor/interceptor';
import {AuthenticationService} from '../../common/application/services/authentication/authentication.service';
import {ProductRepository} from '../domain/repositories/product.repository';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import {FlexLayoutModule, FlexModule} from '@angular/flex-layout';
import {HasPermissionDirective} from '../../common/presentation/has-permission/has-permission';
import {InventoryViewComponent} from './authenticated/stock/inventory-view.component';
import {ProductsViewComponent} from './authenticated/stock/products/products-view.component';
import {
  MAT_FORM_FIELD_DEFAULT_OPTIONS,
  MatFormFieldDefaultOptions,
  MatFormFieldModule
} from '@angular/material/form-field';
import localePt from '@angular/common/locales/pt';
import {MessageService} from '../../common/application/services/message.service';
import {UserRepository} from '../domain/repositories/user.repository';
import {ConsultProductsComponent} from './authenticated/stock/products/consult-products/consult-products.component';
import {InsertProductComponent} from './authenticated/stock/products/insert-product/insert-product.component';
import {UpdateProductComponent} from './authenticated/stock/products/update-product/update-product.component';
import {ViewProductComponent} from './authenticated/stock/products/view-product/view-product.component';
import {ProductFormComponent} from './authenticated/stock/products/insert-product/product-form/product-form.component';
import {DeleteDialogComponent} from '../../common/presentation/crud/delete-dialog/delete-dialog.component';
import {ListPageComponent} from '../../common/presentation/crud/list/list-page.component';
import {CrudViewComponent} from '../../common/presentation/crud/crud-view.component';
// @ts-ignore
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatSliderModule} from '@angular/material/slider';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatSortModule} from '@angular/material/sort';
import {MatStepperModule} from '@angular/material/stepper';
import {MatTableModule} from '@angular/material/table';
import {MatTabsModule} from '@angular/material/tabs';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatDialogModule} from '@angular/material/dialog';
import {DataComponent} from '../../common/presentation/data/data.component';
import {MatChipsModule} from '@angular/material/chips';
import {DialogService} from '../../common/application/services/dialog.service';
import {PaginationService} from '../../common/application/services/pagination.service';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import {MatCardModule} from '@angular/material/card';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatNativeDateModule, MatOptionModule} from '@angular/material/core';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatRadioModule} from '@angular/material/radio';
import {MatSelectModule} from '@angular/material/select';
import {
  ButtonClearFiltersComponent
} from '../../common/presentation/button-clear-filters/button-clear-filters.component';
import {
  ButtonToggleAdvancedFiltersComponent
} from '../../common/presentation/button-toggle-advanced-filters/button-toggle-advanced-filters.component';
import {NoRecordsFoundComponent} from '../../common/presentation/no-records-found/no-records-found.component';
import {
  ProductDataViewComponent
} from './authenticated/stock/products/view-product/product-data-view/product-data-view.component';
import {VerticalSpaceComponent} from '../../common/presentation/vertical-space.component';
import {HorizontalSpaceComponent} from '../../common/presentation/horizontal-space.component';
import {FormPageComponent} from '../../common/presentation/crud/form/form-page.component';
import {ConfigurationViewComponent} from './authenticated/configuration/configuration-view.component';
import {CapitalizePipe} from '../../common/presentation/pipes/capitalize.pipe';
import {FilterPipe} from '../../common/presentation/pipes/filter.pipe';
import {UserInitialsPipe} from '../../common/presentation/pipes/user-initials.pipe';

const appearance: MatFormFieldDefaultOptions = {
  appearance: 'outline'
};

registerLocaleData(localePt, 'pt-BR');

@NgModule({
  declarations: [
    AppRootComponent,

    // Stock
    ProductsViewComponent,
    ProductFormComponent,
    ConsultProductsComponent,
    InsertProductComponent,
    UpdateProductComponent,
    ViewProductComponent,
    ProductDataViewComponent,
    InventoryViewComponent,

    // Configurations
    ConfigurationViewComponent,

    // Generic components
    ButtonToggleAdvancedFiltersComponent,
    ButtonClearFiltersComponent,
    DeleteDialogComponent,
    ListPageComponent,
    AuthenticatedViewComponent,
    CrudViewComponent,
    DataComponent,
    NoRecordsFoundComponent,
    VerticalSpaceComponent,
    HorizontalSpaceComponent,
    FormPageComponent,

    // Pipe
    CapitalizePipe,
    FilterPipe,
    UserInitialsPipe
  ],
  imports: [
    CommonModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,


    ErpRoutingModule,
    HttpClientModule,
    MatToolbarModule,
    MatSlideToggleModule,
    HttpClientJsonpModule,
    MatTooltipModule,
    FlexModule,
    FlexLayoutModule,

    // ANGULAR MATERIAL
    MatAutocompleteModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDatepickerModule,
    MatDialogModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatNativeDateModule,
    MatOptionModule,
    MatPaginatorModule,
    MatRadioModule,
    MatSelectModule,
    MatSidenavModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatSortModule,
    MatStepperModule,
    MatTableModule,
    MatToolbarModule,
    MatTooltipModule,
    MatFormFieldModule,
    MatTabsModule,
    MatExpansionModule,
    MatSliderModule,

    // Has Permission
    HasPermissionDirective,

  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [
    DialogService,
    MessageService,
    UserRepository,
    ProductRepository,
    PaginationService,
    AuthenticationService,
    {
      useValue: appearance,
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS
    },

    {
      multi: true,
      useClass: Interceptor,
      provide: HTTP_INTERCEPTORS
    },
    provideZoneChangeDetection({eventCoalescing: true}),
  ],
  bootstrap: [AppRootComponent]
})
export class ErpModule {
}

