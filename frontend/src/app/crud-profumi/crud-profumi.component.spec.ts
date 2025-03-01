import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CrudProfumiComponent } from './crud-profumi.component';



describe('ProfumiCrudComponent', () => {
  let component: CrudProfumiComponent;
  let fixture: ComponentFixture<CrudProfumiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CrudProfumiComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrudProfumiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
