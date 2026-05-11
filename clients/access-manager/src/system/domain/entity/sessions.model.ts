import {Abstract} from "./abstract/abstract.model";
import {GroupPermission} from "./group-permission.model";

export class Session {

    public id: string;
    public accessTokenExpiresAt: string;
    public refreshTokenExpiresAt: string;

}
