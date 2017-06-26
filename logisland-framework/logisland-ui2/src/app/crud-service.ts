import { RestangularModule, Restangular } from 'ngx-restangular';
import { Injectable, Inject } from '@angular/core';
import { SearchCriteria } from './search-criteria';
import { Http, Response, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Rx'
import { RequestMethod } from '@angular/http';
import {ResourceCRUD, ResourceParams} from 'ngx-resource';

interface QueryInput {
    pageIndex?: number;
    pageSize?: number;
}

@Injectable()
@ResourceParams({
  url: 'http://localhost:8081/'
})
export class CrudService<T> extends ResourceCRUD<QueryInput, T, T> {}
