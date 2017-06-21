import { RestangularModule, Restangular } from 'ngx-restangular';
import { Injectable, Inject } from '@angular/core';

export class SearchCriteria {
    readonly keyword: string;
    readonly pageIndex: number;
    readonly pageSize: number;
}

@Injectable()
export class CrudService<T> {
    readonly entityName: string;

    constructor(@Inject(Restangular) private restangular, entityName: string) {
        console.log('CrudRestService init - entityName: ' + entityName);
        this.entityName = entityName;
    }

    get(id: string): T {
        return null;
    }

    delete(id: string) {

    }

    insert(t: T) {

    }

    update(t: T) {

    }

    search(search: SearchCriteria): T[] {
        return this.restangular.all(this.entityName).getList();
    }
}
