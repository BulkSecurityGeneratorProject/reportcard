(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('CategorieDialogController', CategorieDialogController);

    CategorieDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Categorie', 'Matiere'];

    function CategorieDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Categorie, Matiere) {
        var vm = this;

        vm.categorie = entity;
        vm.clear = clear;
        vm.save = save;
        vm.matieres = Matiere.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.categorie.id !== null) {
                Categorie.update(vm.categorie, onSaveSuccess, onSaveError);
            } else {
                Categorie.save(vm.categorie, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('reportcardApp:categorieUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
