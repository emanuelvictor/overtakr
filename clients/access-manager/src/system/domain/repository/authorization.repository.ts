import {Injectable} from '@angular/core';
import {BaseRepository} from "../../infrastructure/repository/base/base.repository";
import {HttpClient} from "@angular/common/http";
import {Authorization} from "../entity/authorization.model";

@Injectable()
export class AuthorizationRepository extends BaseRepository<Authorization> {

    constructor(httpClient: HttpClient) {
        super(httpClient, 'api/access-manager/authorizations');
    }
}
