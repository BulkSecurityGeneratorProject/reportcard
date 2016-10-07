'use strict';

describe('Controller Tests', function() {

    describe('EstDispense Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEstDispense, MockAnneeAcademique, MockMatiere, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEstDispense = jasmine.createSpy('MockEstDispense');
            MockAnneeAcademique = jasmine.createSpy('MockAnneeAcademique');
            MockMatiere = jasmine.createSpy('MockMatiere');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'EstDispense': MockEstDispense,
                'AnneeAcademique': MockAnneeAcademique,
                'Matiere': MockMatiere,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("EstDispenseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'reportcardApp:estDispenseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
