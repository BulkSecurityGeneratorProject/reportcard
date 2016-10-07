'use strict';

describe('Controller Tests', function() {

    describe('AnneeAcademique Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAnneeAcademique, MockEvaluation, MockInscrire, MockEstDispense;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAnneeAcademique = jasmine.createSpy('MockAnneeAcademique');
            MockEvaluation = jasmine.createSpy('MockEvaluation');
            MockInscrire = jasmine.createSpy('MockInscrire');
            MockEstDispense = jasmine.createSpy('MockEstDispense');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AnneeAcademique': MockAnneeAcademique,
                'Evaluation': MockEvaluation,
                'Inscrire': MockInscrire,
                'EstDispense': MockEstDispense
            };
            createController = function() {
                $injector.get('$controller')("AnneeAcademiqueDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'reportcardApp:anneeAcademiqueUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
