'use strict';

describe('Controller Tests', function() {

    describe('Inscrire Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockInscrire, MockEleve, MockClasse, MockAnneeAcademique;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockInscrire = jasmine.createSpy('MockInscrire');
            MockEleve = jasmine.createSpy('MockEleve');
            MockClasse = jasmine.createSpy('MockClasse');
            MockAnneeAcademique = jasmine.createSpy('MockAnneeAcademique');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Inscrire': MockInscrire,
                'Eleve': MockEleve,
                'Classe': MockClasse,
                'AnneeAcademique': MockAnneeAcademique
            };
            createController = function() {
                $injector.get('$controller')("InscrireDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'reportcardApp:inscrireUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
