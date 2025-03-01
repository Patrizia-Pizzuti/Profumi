import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NoteCrudComponent } from './note-crud.component';

describe('NoteCrudComponent', () => {
  let component: NoteCrudComponent;
  let fixture: ComponentFixture<NoteCrudComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NoteCrudComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NoteCrudComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
