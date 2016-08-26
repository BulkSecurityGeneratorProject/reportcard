'use strict';

describe('Controller Tests', function() {

    describe('Evaluation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEvaluation, MockSequence, MockEleve, MockMatiere;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEvaluation = jasmine.createSpy('MockEvaluation');
            MockSequence = jasmine.createSpy('MockSequence');
            MockEleve = jasmine.createSpy('MockEleve');
            MockMatiere = jasmine.createSpy('MockMatiere');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Evaluation': MockEvaluation,
                'Sequence': MockSequence,
                'Eleve': MockEleve,
                'Matiere': MockMatiere
            };
            createController = function() {
                $injector.get('$controller')("EvaluationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'reportcardApp:evaluationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
