(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('CategorieDetailController', CategorieDetailController);

    CategorieDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Categorie', 'Matiere'];

    function CategorieDetailController($scope, $rootScope, $stateParams, previousState, entity, Categorie, Matiere) {
        var vm = this;

        vm.categorie = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('reportcardApp:categorieUpdate', function(event, result) {
            vm.categorie = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
