// @ts-ignore
import {Component, ViewChild} from '@angular/core';
import {Product} from '../../../../../domain/model/product';
import {MatTableDataSource} from '@angular/material/table';
import {MessageService} from '../../../../../../common/application/services/message.service';
import {ProductRepository} from '../../../../../domain/repositories/product.repository';
import {handlePageable} from '../../../../../../common/infrastructure/utils/handle-data-table';
import {DialogService} from '../../../../../../common/application/services/dialog.service';
import {PaginationService} from '../../../../../../common/application/services/pagination.service';
import {ListPageComponent} from '../../../../../../common/presentation/crud/list/list-page.component';

@Component({
  selector: 'consult-products',
  templateUrl: 'consult-products.component.html', standalone: false
})
export class ConsultProductsComponent /*implements OnInit */ {

  // Bind com o component ListPageComponent
  @ViewChild(ListPageComponent, {static: true})
  private product: Product = new Product();

  public filters: any = {defaultFilter: ''}; // Estado inicial dos filtros

  public pageable: any = {
    size: 20,
    page: 0,
    sort: null,
    defaultFilter: []
  };

  public totalElements: any;
  public pageIndex: any;
  public pageSize: any;

  public columns: any[] = [
    {name: 'name', label: 'Nome'}
  ];

  public displayedColumns: string[] = this.columns.map(cell => cell.name);

  public dataSource = new MatTableDataSource();

  /**
   * @param dialogService {DialogService}
   * @param paginationService {PaginationService}
   * @param messageService {MessageService}
   * @param productRepository {ProductRepository}
   */
  constructor(private dialogService: DialogService,
              paginationService: PaginationService,
              private messageService: MessageService,
              private productRepository: ProductRepository) {

    this.displayedColumns.push('acoes');
    this.pageable = paginationService.pageable('name');

  }

  /**
   *
   */
  ngOnInit() {
    // Seta o size do pageable no size do paginator
    (this.product as any).paginator.pageSize = this.pageable.size;

    // Sobrescreve o sortChange do sort bindado
    this.sortChange();
  }

  /**
   *
   */
  public sortChange() {
    (this.product as any).sort.sortChange.subscribe(() => {
      const {active, direction} = (this.product as any).sort;
      this.pageable.sort = {'properties': active, 'direction': direction};
      this.listByFilters();
    });
  }

  /**
   *
   * @param hasAnyFilter Verifica se há algum filtro,
   * caso exista, então será redirecionado para a primeira página
   */
  public listByFilters(hasAnyFilter: boolean = false) {

    const pageable = handlePageable(hasAnyFilter, (this.product as any).paginator, this.pageable);
    pageable.defaultFilter = (this.product as any).filters.defaultFilter;

    this.productRepository.listByFilters(pageable)
      .subscribe((result: {
        content: unknown[] | undefined;
        totalElements: any;
        size: any;
        pageable: { pageNumber: any; };
      }) => {
        this.dataSource = new MatTableDataSource(result.content);
        this.totalElements = result.totalElements;
        this.pageSize = result.size;
        this.pageIndex = result.pageable.pageNumber;
      });
  }

  /**
   * Função para confirmar a exclusão de um registro permanentemente
   * @param product
   */
  public openDeleteDialog(product: { id: number; }) {

    this.dialogService.confirmDelete(product, 'GRUPO DE ACESSO')
      .then((accept: boolean) => {

        if (accept) {
          this.productRepository.delete(product.id)
            .then(() => {
              this.listByFilters();
              this.messageService.toastSuccess('Produto excluído com sucesso')
            });
        }
      });
  }
}
