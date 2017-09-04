import { DataSourceExt } from './data-source-ext';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';
import { RestService } from './rest-service';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/map';

/**
 * Implementation of a Rest DataSource
 */
export class RestDataSource<T> extends DataSourceExt<T> {

  constructor(private _restService: RestService) {
    super();
  }

  connect(): Observable<T[]> {
    return this._searchFilter.map(searchValue => {
      const items: any[] = [{'name' : 'name1', 'documentation': 'doc1'}, {'name' : 'name2', 'documentation': 'doc2'}];
      return items.filter(item => {
        const searchStr = (item.name + item.documentation).toLowerCase();
        return searchStr.indexOf(searchValue.toLowerCase()) !== -1;
      }
      );
    });
  }

  disconnect() { }

  search(): Observable<T[]> {
    return this.connect();
  }
}
