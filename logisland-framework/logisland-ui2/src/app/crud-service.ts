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

@ResourceParams({
})
export class CrudService<T> extends ResourceCRUD<QueryInput, T, T> {
  constructor(private url: string, http: Http) {
    super(http);
    super.$setUrl(url);
  }
}
