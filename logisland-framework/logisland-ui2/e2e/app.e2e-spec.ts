import { LogislandUi2Page } from './app.po';

describe('logisland-ui2 App', () => {
  let page: LogislandUi2Page;

  beforeEach(() => {
    page = new LogislandUi2Page();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!!');
  });
});
