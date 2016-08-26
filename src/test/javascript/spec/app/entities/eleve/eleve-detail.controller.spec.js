'use strict';

describe('Controller Tests', function() {

    describe('Eleve Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEleve, MockEvaluation, MockInscrire;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEleve = jasmine.createSpy('MockEleve');
            MockEvaluation = jasmine.createSpy('MockEvaluation');
            MockInscrire = jasmine.createSpy('MockInscrire');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Eleve': MockEleve,
                'Evaluation': MockEvaluation,
                'Inscrire': MockInscrire
            };
            createController = function() {
                $injector.get('$controller')("EleveDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'reportcardApp:eleveUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
