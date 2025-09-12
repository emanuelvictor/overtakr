// @ts-ignore
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticatedViewComponent} from '../../../authenticated-view.component';
import {Product} from '../../../../../domain/model/product';
import {DialogService} from '../../../../../../common/application/services/dialog.service';
import {MessageService} from '../../../../../../common/application/services/message.service';
import {ProductRepository} from '../../../../../domain/repositories/product.repository';

@Component({
  selector: 'view-products',
  templateUrl: 'view-product.component.html', standalone: false
})
export class ViewProductComponent implements OnInit {

  /**
   *
   */
  product: Product = new Product();

  /**
   *
   * @param router
   * @param homeView
   * @param dialogService
   * @param activatedRoute
   * @param messageService
   * @param productRepository
   */
  constructor(private router: Router,
              private dialogService: DialogService,
              public activatedRoute: ActivatedRoute,
              private messageService: MessageService,
              private homeView: AuthenticatedViewComponent,
              private productRepository: ProductRepository) {
    this.product.id = this.activatedRoute.snapshot.params['id'] || null;
    this.homeView.toolbar.subhead = 'Produto / Detalhes';
  }

  /**
   *
   */
  ngOnInit() {
    if (this.product && this.product.id) {
      this.findById();
    }
  }

  /**
   *
   */
  public findById() {
    this.productRepository.findById(this.product.id)
      .subscribe((result: Product) => {
        this.product = result;
      })
  }

  /**
   * Função para confirmar a exclusão de um registro permanentemente
   * @param product
   */
  public openDeleteDialog(product: Product) {

    this.dialogService.confirmDelete(product, 'Produto')
      .then((accept: boolean) => {

        if (accept) {
          this.productRepository.delete(product.id)
            .then(() => {
              this.router.navigate(['stocks/products']);
              this.messageService.toastSuccess('Registro excluído com sucesso')
            })
        }
      })
  }
}
