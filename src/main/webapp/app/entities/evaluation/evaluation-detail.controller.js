(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('EvaluationDetailController', EvaluationDetailController);

    EvaluationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Evaluation', 'Sequence', 'Eleve', 'Matiere'];

    function EvaluationDetailController($scope, $rootScope, $stateParams, previousState, entity, Evaluation, Sequence, Eleve, Matiere) {
        var vm = this;

        vm.evaluation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('reportcardApp:evaluationUpdate', function(event, result) {
            vm.evaluation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
