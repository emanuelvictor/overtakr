import {Injectable} from '@angular/core';
import {BaseRepository} from "../../infrastructure/repository/base/base.repository";
import {HttpClient} from "@angular/common/http";
import {Group} from "../entity/group.model";
import {Session} from "../entity/sessions.model";

@Injectable()
export class SessionRepository extends BaseRepository<Session> {

  constructor(httpClient: HttpClient) {
    super(httpClient, 'api/access-manager/sessions');
  }
}
