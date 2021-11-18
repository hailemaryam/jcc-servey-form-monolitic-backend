package et.com.hmmk.rmt;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("et.com.hmmk.rmt");

        noClasses()
            .that()
            .resideInAnyPackage("et.com.hmmk.rmt.service..")
            .or()
            .resideInAnyPackage("et.com.hmmk.rmt.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..et.com.hmmk.rmt.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
