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
import {SessionRepository} from "../../../../../../../domain/repository/session.repository";
import {MatTableDataSource} from "@angular/material/table";
import {handlePageable} from "../../../../../../utils/handle-data-table";
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {tap} from "rxjs/operators";
import {AuthenticationService} from "../../../../../../../domain/services/authentication.service";

const appearance: MatFormFieldDefaultOptions = {
    appearance: 'outline'
};

// @ts-ignore
@Component({
    selector: 'consult-sessions',
    templateUrl: 'consult-sessions.component.html',
    animations: [tdCollapseAnimation],
    providers: [
        {
            provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
            useValue: appearance
        }
    ]
})
export class ConsultSessionsComponent implements OnInit {

    @ViewChild(MatMenuTrigger, {static: true}) trigger: MatMenuTrigger;
    @ViewChild(MatPaginator, {static: true}) public paginator: MatPaginator; // Bind com o objeto paginator
    @ViewChild(MatSort, {static: true}) sort: MatSort; // Bind com objeto sort

    @Input() principalName: string;

    // Tabela
    @Input() dataSource: any;
    displayedColumns: string[] = ['sid', 'principalName', 'acoes'];
    @Input() totalElements: any;
    @Input() pageSize: any;
    @Input() pageIndex: any;

    public pageable: any = {
        size: 20,
        page: 0,
        sort: null,
        defaultFilter: []
    };

    public constructor(private sessionRepository: SessionRepository) {
    }

    ngOnInit() {
        this.listByFilters();
    }

    listByFilters = () => {
        const pageable = handlePageable(false, this.paginator, this.pageable);
        pageable.principalName = this.principalName;
        this.sessionRepository.listByFilters(pageable).subscribe(result => {
            this.dataSource = new MatTableDataSource(result.content);
            this.totalElements = result.totalElements;
            this.pageSize = result.size;
            this.pageIndex = result.pageNumber;
        });
        this.paginator.pageSize = this.pageSize;
    };

    delete(sid: string): void {
        this.sessionRepository.delete(sid).then(() => {
            this.listByFilters()
        });
    }

}
