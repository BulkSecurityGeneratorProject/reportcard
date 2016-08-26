'use strict';

describe('Controller Tests', function() {

    describe('Matiere Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMatiere, MockCategorie, MockEstDispense, MockEvaluation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMatiere = jasmine.createSpy('MockMatiere');
            MockCategorie = jasmine.createSpy('MockCategorie');
            MockEstDispense = jasmine.createSpy('MockEstDispense');
            MockEvaluation = jasmine.createSpy('MockEvaluation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Matiere': MockMatiere,
                'Categorie': MockCategorie,
                'EstDispense': MockEstDispense,
                'Evaluation': MockEvaluation
            };
            createController = function() {
                $injector.get('$controller')("MatiereDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'reportcardApp:matiereUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
