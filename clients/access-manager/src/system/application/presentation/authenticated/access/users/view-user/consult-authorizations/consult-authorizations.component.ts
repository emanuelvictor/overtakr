import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {
    MAT_FORM_FIELD_DEFAULT_OPTIONS,
    MatFormFieldDefaultOptions,
    MatMenuTrigger,
    MatPaginator,
    MatSort
} from '@angular/material';
import {tdCollapseAnimation} from '@covalent/core';
import {debounce} from '../../../../../../utils/debounce';
import {AuthorizationRepository} from "../../../../../../../domain/repository/authorization.repository";
import {MatTableDataSource} from "@angular/material/table";
import {handlePageable} from "../../../../../../utils/handle-data-table";

const appearance: MatFormFieldDefaultOptions = {
    appearance: 'outline'
};

// @ts-ignore
@Component({
    selector: 'consult-authorizations',
    templateUrl: 'consult-authorizations.component.html',
    animations: [tdCollapseAnimation],
    providers: [
        {
            provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
            useValue: appearance
        }
    ]
})
export class ConsultAuthorizationsComponent implements OnInit {

    @ViewChild(MatMenuTrigger, {static: true}) trigger: MatMenuTrigger;
    @ViewChild(MatPaginator, {static: true}) public paginator: MatPaginator; // Bind com o objeto paginator
    @ViewChild(MatSort, {static: true}) sort: MatSort; // Bind com objeto sort

    @Input() principalName: string;

    // Tabela
    @Input() dataSource: any;
    displayedColumns: string[] = ['sid', 'principalName', 'token', 'acoes'];
    @Input() totalElements: any;
    @Input() pageSize: any;
    @Input() pageIndex: any;

    public pageable: any = {
        size: 20,
        page: 0,
        sort: null,
        defaultFilter: []
    };

    public constructor(private authorizationRepository: AuthorizationRepository) {
    }

    ngOnInit() {
        this.listByFilters();
    }

    listByFilters = () => {
        const pageable = handlePageable(false, this.paginator, this.pageable);
        pageable.principalName = this.principalName;
        this.authorizationRepository.listByFilters(pageable).subscribe(result => {
            this.dataSource = new MatTableDataSource(result.content);
            this.totalElements = result.totalElements;
            this.pageSize = result.size;
            this.pageIndex = result.pageNumber;
        });
        this.paginator.pageSize = this.pageSize;
    };

    delete(token: string): void {
        this.authorizationRepository.delete(token).then(() => {
            this.listByFilters()
        });
    }

}
