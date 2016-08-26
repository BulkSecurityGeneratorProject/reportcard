(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('MatiereDetailController', MatiereDetailController);

    MatiereDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Matiere', 'Categorie', 'EstDispense', 'Evaluation'];

    function MatiereDetailController($scope, $rootScope, $stateParams, previousState, entity, Matiere, Categorie, EstDispense, Evaluation) {
        var vm = this;

        vm.matiere = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('reportcardApp:matiereUpdate', function(event, result) {
            vm.matiere = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
