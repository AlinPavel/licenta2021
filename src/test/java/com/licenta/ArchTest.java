package com.licenta;

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
            .importPackages("com.licenta");

        noClasses()
            .that()
            .resideInAnyPackage("com.licenta.service..")
            .or()
            .resideInAnyPackage("com.licenta.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.licenta.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
