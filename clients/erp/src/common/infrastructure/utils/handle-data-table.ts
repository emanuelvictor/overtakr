import {MatPaginator} from '@angular/material/paginator';

/**
 * Trata a paginação e devolve ela atualizada.
 *
 * @param hasAnyFilter Verifica se há algum filtro, caso exista, então será redirecionado para a primeira página
 * @param paginator
 * @param pageable
 */
export function handlePageable(hasAnyFilter: boolean, paginator: MatPaginator, pageable: any) {
  const pageSize = paginator.pageSize;
  const pageIndex = paginator.pageIndex;
  let refreshPageable = Object.assign({}, pageable);
  refreshPageable.size = pageSize;
  refreshPageable.page = !hasAnyFilter ? pageIndex : 0;
  return refreshPageable
}
