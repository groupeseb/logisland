import { RestangularModule, Restangular } from 'ngx-restangular';
import { Injectable, Inject } from '@angular/core';
import { SearchCriteria } from './search-criteria';

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
