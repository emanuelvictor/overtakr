import {DialogService} from '../../../../../../domain/services/dialog.service';
import {MessageService} from '../../../../../../domain/services/message.service';
import {PaginationService} from '../../../../../../domain/services/pagination.service';
import {UserRepository} from "../../../../../../domain/repository/user.repository";
import {Component} from "@angular/core";
import {HttpClient} from "@angular/common/http";

// @ts-ignore
@Component({
    selector: 'consultar-users',
    templateUrl: './test-access.component.html',
    styleUrls: ['../user.component.scss']
})
export class TestAccessComponent {

    /**
     * @param httpClient
     * @param messageService
     */
    constructor(private httpClient: HttpClient,
                private messageService: MessageService) {
    }

    public() {
        this.httpClient.get('http://localhost:8080/api/flows/test/public')
            .subscribe((response: any) => {
                console.log(response);
                this.messageService.toastSuccess(`Funcionou.`, 5)
            });
    }

    secure() {
        this.httpClient.get('http://localhost:8080/api/flows/test/secure')
            .subscribe((response: any) => {
                console.log(response);
                this.messageService.toastSuccess(`Funcionou.`, 5)
            });
    }

    claims() {
        this.httpClient.get('http://localhost:8080/api/flows/test/claims')
            .subscribe((response: any) => {
                console.log(response);
                this.messageService.toastSuccess(`Funcionou.`, 5)
            });
    }

    nonExistentAuthority() {
        this.httpClient.get('http://localhost:8080/api/flows/test/non-existent-authority')
            .subscribe((response: any) => {
                console.log(response);
            });
    }

    root() {
        this.httpClient.get('http://localhost:8080/api/flows/test/root')
            .subscribe((response: any) => {
                console.log(response);
                this.messageService.toastSuccess(`Funcionou.`, 5)
            });
    }
}

